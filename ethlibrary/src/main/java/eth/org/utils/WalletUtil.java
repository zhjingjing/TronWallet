package eth.org.utils;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.security.SecureRandom;

import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.SeedCalculator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;
import io.github.novacrypto.hashing.Sha256;

/**
 * create by zj on 2019/1/15
 */
public class WalletUtil {

    //生成一组助记词
    public static   String generateMnemonics() {
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[Words.TWELVE.byteLength()];
        new SecureRandom().nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE)
                .createMnemonic(entropy, sb::append);
        return sb.toString();
    }


    //根据助记词生成种子
    public static byte[] getSeed(String mnemonic){
        byte[] seed = new SeedCalculator().calculateSeed(mnemonic, "");
        return  seed;
    }

    public static  ECKeyPair getECKeyPair(byte[] seed){
        ECKeyPair ecKeyPair = ECKeyPair.create(Sha256.sha256(seed));
        return  ecKeyPair;
    }

    //创建钱包
    public static WalletFile createLight(String pwd,ECKeyPair ecKeyPair){
        WalletFile walletFile = null;
        try {
            walletFile = Wallet.createLight(pwd, ecKeyPair);
        } catch (CipherException e) {
            e.printStackTrace();
        }finally {
            return walletFile;
        }
    }



}
