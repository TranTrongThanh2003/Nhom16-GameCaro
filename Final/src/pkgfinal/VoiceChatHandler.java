package pkgfinal;

import javax.sound.sampled.*;
import javax.swing.JOptionPane;

import java.io.*;
import java.net.*;

public class VoiceChatHandler {
    private static final int PORT = 1235; // Port khác với port game
    private static final int SAMPLE_RATE = 16000;
    private static final int SAMPLE_SIZE_IN_BITS = 16;
    private static final int CHANNELS = 1;
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = false;
    
    private Socket voiceSocket;
    private AudioFormat format;
    private TargetDataLine microphone;
    private SourceDataLine speakers;
    private boolean isRunning = false;
    
    public VoiceChatHandler() {
        format = new AudioFormat(16000.0f, 16, 1, true, false);
        

    }
    
    // Phương thức cho server
    public void startVoiceServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Voice server waiting for connection...");
                voiceSocket = serverSocket.accept();
                System.out.println("Voice client connected!");
                startVoiceCommunication();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    // Phương thức cho client
    public void connectToVoiceServer(String serverIP) {
        new Thread(() -> {
            try {
                voiceSocket = new Socket(serverIP, PORT);
                System.out.println("Connected to voice server!");
                startVoiceCommunication();
            } catch (Exception e) {
                //JOptionPane.showMessageDialog(null, "Không thể kết nối voice chat", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }).start();
    }
    
    private void startVoiceCommunication() {
        isRunning = true;
        
        // Thread ghi âm
        new Thread(() -> {
            try {
                DataOutputStream out = new DataOutputStream(voiceSocket.getOutputStream());
                microphone = AudioSystem.getTargetDataLine(format);
                
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);
                microphone.start();
                
                byte[] buffer = new byte[2048];
                while (isRunning) {
                    int bytesRead = microphone.read(buffer, 0, buffer.length);
                    if (bytesRead > 0 && isAboveThreshold(buffer, bytesRead)) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        
        // Thread phát âm
        new Thread(() -> {
            try {
                DataInputStream in = new DataInputStream(voiceSocket.getInputStream());
                
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                speakers = (SourceDataLine) AudioSystem.getLine(info);
                speakers.open(format);
                speakers.start();
                
                byte[] buffer = new byte[2048];
                while (isRunning) {
                    int bytesRead = in.read(buffer, 0, buffer.length);
                    if (bytesRead > 0) {
                        speakers.write(buffer, 0, bytesRead);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private boolean isAboveThreshold(byte[] buffer, int length) {
        int threshold = 500; // Thử nghiệm số này, có thể tăng hoặc giảm
        for (int i = 0; i < length - 1; i += 2) {
            // Lấy mẫu âm thanh 16-bit từ 2 byte (big endian)
            int sample = (buffer[i] << 8) | (buffer[i + 1] & 0xFF);
            if (Math.abs(sample) > threshold) {
                return true;
            }
        }
        return false;
    }
    
    
    public void stopVoiceChat() {
        isRunning = false;
        if (microphone != null) {
            microphone.stop();
            microphone.close();
        }
        if (speakers != null) {
            speakers.stop();
            speakers.close();
        }
        try {
            if (voiceSocket != null) {
                voiceSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}