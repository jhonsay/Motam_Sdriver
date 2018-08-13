package com.cryptoull.motam_sdriver;

/**
 * Created by jhonsay on 30/5/18.
 */

class Comunicador {
    private static Object objeto = null;

    public static void setObjeto(Object newObjeto) {
        objeto = newObjeto;
    }

    public static Object getObjeto() {
        return objeto;
    }
}
