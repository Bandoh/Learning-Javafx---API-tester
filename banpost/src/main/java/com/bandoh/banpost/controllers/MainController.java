package com.bandoh.banpost.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import com.bandoh.banpost.Helpers.enums.ReqType;
import com.bandoh.banpost.Helpers.MainControllerHelper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MainController implements Initializable {

    MainControllerHelper mh = new MainControllerHelper();
    // Controls
    public ImageView close_btn;
    public Button send_btn;
    public TextField url_field;
    public WebView wb;
    public MenuButton reqtype_btn;
    public TextArea response_field;
    public ListView<HBox> header_fields;
    public ListView<HBox> body_fields;
    public Button switch_header_btn;
    public Button switch_body_btn;
    private String body;
    private String headers;

    // Actions
    public void closeApp() {
        Stage stage = (Stage) close_btn.getScene().getWindow();
        stage.close();
    }

    private void setFields(ListView<HBox> n) {
        Insets myPadding = new Insets(8.0, 0.0, 0.0, 0.0);
        String l = String.valueOf(n.getItems().size() + 1);
        Label num = new Label(l);
        num.setPadding(myPadding);
        num.setStyle("-fx-background-color:-fx-prim");
        n.setStyle("-fx-background-color:none;");
        HBox hBox = new HBox(10);
        Label k = new Label("Key");
        k.setPadding(myPadding);
        hBox.getChildren().addAll(num, k, new TextField());
        Pane pane = new Pane();
        hBox.setStyle("-fx-background-color:none;");
        pane.getChildren().add(hBox);

        HBox shBox = new HBox(10);
        Label v = new Label("Value");
        v.setPadding(myPadding);
        shBox.getChildren().addAll(v, new TextField());
        Pane spane = new Pane();
        shBox.setStyle("-fx-background-color:none;");
        spane.getChildren().add(shBox);

        HBox comb = new HBox(30);
        comb.setId("row" + l);
        comb.getChildren().addAll(pane, spane);
        n.getItems().addAll(comb);
    }

    public void addField() {
        if (body_fields.isVisible())
            setFields(body_fields);
        if (header_fields.isVisible())
            setFields(header_fields);

    }

    private String getH(ListView<HBox> n) {
        String com = "{";
        for (HBox i : n.getItems()) {
            // Key
            Pane p = (Pane) i.getChildren().get(0);
            HBox h = (HBox) p.getChildren().get(0);
            TextField tf = (TextField) h.getChildren().get(2);
            // Value
            Pane p1 = (Pane) i.getChildren().get(1);
            HBox h1 = (HBox) p1.getChildren().get(0);
            TextField tf1 = (TextField) h1.getChildren().get(1);
            if (n.equals(body_fields)) {
                if (n.getItems().get(n.getItems().size() - 1).equals(i)) {
                    com += "\"" + tf.getText() + "\"" + ":" + "\"" + tf1.getText() + "\"" + "}";
                } else {
                    com += "\"" + tf.getText() + "\"" + ":" + "\"" + tf1.getText() + "\"" + ",";
                }
            } else if (n.equals(header_fields)) {
                if (n.getItems().get(n.getItems().size() - 1).equals(i)) {
                    com += tf.getText() + "," + tf1.getText() + "}";
                } else {
                    com += tf.getText() + "," + tf1.getText() + ";";
                }
            }
        }
        return com;
    }

    // private void basicAnim(Node n) {
    // FadeTransition fd = new FadeTransition();
    // fd.setNode(n);
    // fd.setFromValue(1.0);
    // fd.setToValue(0.0);
    // fd.setDuration(new Duration(500));
    // fd.setCycleCount(1);
    // fd.play();
    // }

    public void switchHeader() {
        body_fields.setVisible(false);
        header_fields.setVisible(true);
        // header_fields.setVisible(false);
    }

    public void switchBody() {
        body_fields.setVisible(true);
        header_fields.setVisible(false);
    }

    public void sendRequest() throws IOException, InterruptedException, ExecutionException {
        System.out.println(mh.getReqType());
        System.out.println(reqtype_btn);
        String data = url_field.getText();
        if (!data.isEmpty()) {
            if (!data.contains("http")) {
                data = "http://" + data;
            }

            WebEngine we = wb.getEngine();
            this.body = getH(body_fields);
            this.headers = getH(header_fields);
            System.out.println(this.body);
            System.out.println(this.headers);
            String[] res = mh.reqHandler(data, this.body, this.headers);
            wb.setVisible(true);
            if (res[1].contains("image")) {
                we.load(data);
            } else
                we.loadContent(res[0], res[1]);
            we.setUserStyleSheetLocation("data:,body { background-color: #001f3f; color:#83e85a }");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        switch_body_btn.setVisible(false);
        body_fields.setVisible(false);
        reqtype_btn.getItems().clear();
        MenuItem get = new MenuItem("GET");
        get.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                switch_body_btn.setVisible(false);
                mh.setReqType(ReqType.GET);
                reqtype_btn.setText("GET");
            }
        });
        MenuItem post = new MenuItem("POST");
        post.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                switch_body_btn.setVisible(true);
                mh.setReqType(ReqType.POST);
                reqtype_btn.setText("POST");
            }
        });
        reqtype_btn.getItems().addAll(get, post);
        mh.setReqType(ReqType.GET);
    }

}
