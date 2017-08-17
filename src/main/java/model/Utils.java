package model;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by sergeybp on 20.07.17.
 */
public class Utils {

    public String generateNewToken() {
        Random random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }


}
