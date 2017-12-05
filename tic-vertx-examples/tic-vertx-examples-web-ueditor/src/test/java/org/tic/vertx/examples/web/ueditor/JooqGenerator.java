package org.tic.vertx.examples.web.ueditor;

import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;

import javax.xml.bind.JAXB;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

public class JooqGenerator {

    public static void main(String[] args) {
        try {
            URL url = JooqGenerator.class.getClassLoader().getResource("jooq-codegen.xml");
            if (url == null) {
                System.out.println("file not found");
                return;
            }
            String s = URLDecoder.decode(url.getFile(), "utf-8");
            Configuration configuration = JAXB.unmarshal(new File(s), Configuration.class);
            GenerationTool.generate(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
