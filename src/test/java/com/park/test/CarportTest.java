//package com.park.test;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import com.park.model.Carport;
//import com.park.service.CarportService;
//
//public class CarportTest {
//
//	private CarportService carportService;
//	
//	@Before
//    public void before(){                                                                    
//        @SuppressWarnings("resource")
//        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:conf/spring.xml"
//                ,"classpath:conf/spring-mybatis.xml"});
//        carportService = (CarportService) context.getBean("CarportServiceImpl");
//    }
//	
//	@Test
//    public void addUser(){
//		Carport carport = new Carport();
//        carport.setId(1);
//        carport.setState(0);
//        carport.setDesc("free");
//        System.out.println(carportService.insertCarport(carport));
//    }
//}
