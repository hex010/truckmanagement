module truck.truckmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.persistence;
    requires mysql.connector.java;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires junit;
    requires javafx.graphics;

    opens truck.truckmanagement to javafx.fxml;
    exports truck.truckmanagement;
    opens truck.truckmanagement.Controller;
    exports truck.truckmanagement.Controller;

    opens truck.truckmanagement.Model to org.hibernate.orm.core;

    opens truck.truckmanagement.TableParameters to javafx.base;
}