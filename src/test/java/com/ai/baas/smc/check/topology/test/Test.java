package com.ai.baas.smc.check.topology.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.ai.baas.smc.check.topology.constants.SmcConstant;

public class Test {

    public static void main(String[] args) throws IOException {
        String cvsFileName = "ERR_test.csv";
        File csvFile = null;
        BufferedWriter writer = null;
        String tmpPath = "e:/test";
        csvFile = new File(tmpPath + "/" + cvsFileName);
        File parent = csvFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        csvFile.createNewFile();
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile),
                "gb2312"));
        // 写入文件头部
        writer.write("批次号");
        writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
        writer.write("batch_no");
        writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
        writer.write("总数量");
        writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
        writer.write(String.valueOf(3));
        writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
        writer.write("本文件记录数");
        writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
        writer.flush();
        writer.close();
        System.out.println("ok");
    }

}
