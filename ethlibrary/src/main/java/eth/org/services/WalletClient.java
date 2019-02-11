package eth.org.services;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

/**
 * create by zj on 2019/1/14
 */
public class WalletClient {

    public static Web3j build(){
        //rinkeby
//        return Web3jFactory.build(new HttpService("https://rinkeby.infura.io/v3/9903add979c54c21872ed1388954129d"));

        //mainnet
        return Web3jFactory.build(new HttpService("https://mainnet.infura.io/v3/9903add979c54c21872ed1388954129d"));
    }

}
