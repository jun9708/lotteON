package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Cate02;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class Cate02RepositoryTests {


    @Autowired
    private Cate02Repository cate02Repository;

    @Test
    public void test1(){
        List<Cate02> result = cate02Repository.findByCate01No("AA");
        log.info(result);
    }


}