package tests;

import application.Encryptor;
import junit.framework.TestCase;
import org.junit.Test;

public class EncryptorTests extends TestCase {
    @Test
    public void testLength0() {
        String plainText = "";
        String cipherText = Encryptor.encrypt(plainText);
        System.out.println(plainText + "->" + cipherText);
        assertEquals(cipherText.length(), 15);
    }

    @Test
    public void testLength1() {
        String plainText = "5";
        String cipherText = Encryptor.encrypt(plainText);
        System.out.println(plainText + "->" + cipherText);
        assertEquals(cipherText.length(), 15);
        plainText = "k";
        cipherText = Encryptor.encrypt(plainText);
        System.out.println(plainText + "->" + cipherText);
        assertEquals(cipherText.length(), 15);
    }

    @Test
    public void testLength5() {
        String plainText = "mdX9h";
        String cipherText = Encryptor.encrypt(plainText);
        System.out.println(plainText + "->" + cipherText);
        assertEquals(cipherText.length(), 15);
        plainText = "3mD]*";
        cipherText = Encryptor.encrypt(plainText);
        System.out.println(plainText + "->" + cipherText);
        assertEquals(cipherText.length(), 15);
    }

    @Test
    public void testLength9() {
        String plainText = "FX?zrnUG3";
        String cipherText = Encryptor.encrypt(plainText);
        System.out.println(plainText + "->" + cipherText);
        assertEquals(cipherText.length(), 15);
        plainText = "QpA&K5r6P";
        cipherText = Encryptor.encrypt(plainText);
        System.out.println(plainText + "->" + cipherText);
        assertEquals(cipherText.length(), 15);
    }

    @Test
    public void testLength15() {
        String plainText = "b[x-:m_f;-a89^F";
        String cipherText = Encryptor.encrypt(plainText);
        System.out.println(plainText + "->" + cipherText);
        assertEquals(cipherText.length(), 15);
        plainText = ";NW$YC?3_a#ub!t";
        cipherText = Encryptor.encrypt(plainText);
        System.out.println(plainText + "->" + cipherText);
        assertEquals(cipherText.length(), 15);
    }
}
