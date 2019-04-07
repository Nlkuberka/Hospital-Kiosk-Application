package application;

public class Encryptor {
    // A matrix that encrypts blocks of 5 characters
    private static final int[][] ENCRYPTION_MATRIX = new int[][] {
            {149, 146,   0, 224, 170},
            {171, 135, 107, 122, 112},
            {237,  85,  57, 103, 201},
            {252, 145, 136,   4,  91},
            {  2,  72, 136,  69,  30}
    };
    public static final int MAX_PASSWORD_LENGTH = 15;
    public static String encrypt(String password) {
        if(password.length() > 15) {    // if the input exceeds maximum password length
            return null;
        }
        // Pad the input with spaces until it is 15 characters long.
        while(password.length() < 15) {
            password = password + ' ';
        }
        String cipherText = ""; // the encrypted password
        for(int block = 0; block < MAX_PASSWORD_LENGTH; block += ENCRYPTION_MATRIX.length) {    // for each block of five characters
            String substring = password.substring(block, block + ENCRYPTION_MATRIX.length);
            // Multiply the encryption matrix by the substring (interpreted as a vector of integers) (mod 256) to get a new string of length 5.
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
