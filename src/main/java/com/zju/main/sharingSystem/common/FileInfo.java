package com.zju.main.sharingSystem.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {

    private String name;

    private String size;

    private String time;

}
