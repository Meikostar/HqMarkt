package com.hqmy.market.bean;

import java.io.Serializable;

public class WEIXINREQ implements Serializable {
    public String sign;
    public String timestamp;
    public String noncestr;
    //    public String noncestr;
    public String prepayid;
    public String mch_id;
    public WEIXINREQ data;

    public String packages;//="Sign=WXPay"
    public String appid;
    public String partnerid;
    public String orderId;

//        "appid": "wx3d60d453e26acc76",
//                "partnerid": "1554011131",
//                "prepayid": "wx091651223048374b2109cf351145072000",
//                "timestamp": "1568019082",
//                "noncestr": "B8uXmnR7EtDv2wcn",
//                "package": "Sign=WXPay",
//                "sign": "A6C4EEACBD77846F586E0533E959065C"

    //    "package": "Sign=WXPay",
    //            "orderId": "1000001275057876",
    //            "appid": "wxed0453751545a174",
    //            "sign": "5049D38F222F7421C31197D2F174FBC3",
    //            "partnerid": "1496042602",
    //            "prepayid": "wx181737247031626a567e36550344247913",
    //            "noncestr": "wHqh4BiNmzGWkNarREGe8vc3WCBKJuhz",
    //            "timestamp": "1531906628"
    //        "package": "Sign=WXPay",
    //                "appid": "wxed0453751545a174",
    //                "sign": "A2A0D71D3EAB1FE1F8216EE940FCC25F",
    //                "partnerid": "1496042602",
    //                "prepayid": "wx2018020212015946b9238c500089992428",
    //                "noncestr": "2ZKiLOzWDC6rooI91KMSYis5wi6xynzg",
    //                "timestamp": "1517544131"


}


