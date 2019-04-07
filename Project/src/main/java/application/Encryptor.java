package application;

public class Encryptor {
    private static final int[][] ENCRYPTION_MATRIX = new int[][] {
            {149, 146,   0, 224, 170},
            {171, 135, 107, 122, 112},
            {237,  85,  57, 103, 201},
            {252, 145, 136,   4,  91},
            {  2,  72, 136,  69,  30}
    };
    public static final int MAX_PASSWORD_LENGTH = 15;
    public static String encrypt(String password) {
        if(password.length() > 15) {
            return null;
        }
        while(password.length() < 15) {
            password = password + ' ';
        }
        String cipherText = "";
        for(int block = 0; block < MAX_PASSWORD_LENGTH; block += ENCRYPTION_MATRIX.length) {
            String substring = password.substring(block, block + ENCRYPTION_MATRIX.length);
            for(int i = 0; i < ENCRYPTION_MATRIX.length; i++) {
                char encryptedChar = 0;
                for(int j = 0; j < ENCRYPTION_MATRIX.length; j++) {
                    encryptedChar = (char) ((encryptedChar + substring.charAt(j) * ENCRYPTION_MATRIX[i][j]) % 256);
                }
                cipherText = cipherText + encryptedChar;
            }

        }
        return cipherText;
    }
}
