package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.dao.implementations.GroupDaoImpl;
import ua.com.foxminded.university.models.Group;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        
        GroupDaoImpl groupDao = context.getBean("groupDaoImpl", GroupDaoImpl.class);
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Group expected = group;
        Group actual = groupDao.getById(1);
        System.out.println(expected);
        System.out.println(actual);

        context.close();
    }

}
