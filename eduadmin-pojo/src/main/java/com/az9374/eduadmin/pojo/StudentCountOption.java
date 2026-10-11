package com.az9374.eduadmin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentCountOption {

/*        "data": {
            "clazzList": ["Java就业100期","Java就业101期","Java就业102期","Java就业103期","Java就业104期"],
            "dataList": [77,82,70,80,90]
        }*/
    private List<Object> clazzList;
    private List<Object> dataList;
}
