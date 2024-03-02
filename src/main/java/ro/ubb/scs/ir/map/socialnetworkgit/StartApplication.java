package ro.ubb.scs.ir.map.socialnetworkgit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.CerereValidator;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.MessageValidator;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.PrietenieValidator;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.UtilizatorValidator;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.CerereDBRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.MessageDBRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.PrieteniiDBRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.UtilizatorDBRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.PagingRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.service.CereriService;
import ro.ubb.scs.ir.map.socialnetworkgit.service.MessageService;
import ro.ubb.scs.ir.map.socialnetworkgit.service.PrieteniiService;
import ro.ubb.scs.ir.map.socialnetworkgit.service.UtilizatorService;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.UtilizatorValidator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class StartApplication extends Application {
    PagingRepository repo_db;
    UtilizatorService srv;

    MessageDBRepository mess_db;

    MessageService srv_m;

    CerereDBRepository cerereDBRepository;

    CereriService srv_c;

    PrieteniiDBRepository prieteniiDBRepository;

    PrieteniiService srv_p;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Retea de socializare");

        repo_db = new UtilizatorDBRepository(new UtilizatorValidator(), "jdbc:postgresql://localhost:5432/socialNetwork","postgres","tpss01060110");
        srv = new UtilizatorService((UtilizatorDBRepository) repo_db);

        mess_db = new MessageDBRepository(new MessageValidator(), "jdbc:postgresql://localhost:5432/socialNetwork","postgres","tpss01060110");
        srv_m = new MessageService(mess_db);

        cerereDBRepository = new CerereDBRepository(new CerereValidator(), "jdbc:postgresql://localhost:5432/socialNetwork","postgres","tpss01060110");
        srv_c = new CereriService(cerereDBRepository);

        prieteniiDBRepository = new PrieteniiDBRepository(new PrietenieValidator(), "jdbc:postgresql://localhost:5432/socialNetwork","postgres","tpss01060110");
        srv_p = new PrieteniiService(prieteniiDBRepository);

        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/user_view.fxml"));
            AnchorPane layout = loader.load();
            primaryStage.setScene(new Scene(layout));

            UserController userController = loader.getController();
            userController.setUserService(srv, srv_m, srv_c, srv_p);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
