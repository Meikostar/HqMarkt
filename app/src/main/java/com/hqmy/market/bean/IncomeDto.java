package com.hqmy.market.bean;

import java.io.Serializable;

public class IncomeDto implements Serializable {
//                "id": 4,
//                        "user_id": 1,
//                        "value": "11.00",
//                        "type": "admin",
//                        "type_id": 4318,
//                        "wallet_type": "money",
//                        "created_at": "2019-10-17 09:38:25",
//                        "updated_at": "2019-10-17 09:38:25",
//                        "type_name": "后台操作",
//                        "wallet_type_name": "现金"

    public String user_id;
    public String value;
    public String type;
    public String type_id;
    public String wallet_type;
    public String created_at;
    public String updated_at;
    public String type_name;
    public String wallet_type_name;

    public int id;

}
