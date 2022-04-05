package com.company.GUI.InputGUI;

import com.company.GUI.DB.DBHandler;
import com.company.GUI.DB.Material;
import com.company.GUI.DataHandler;
import com.company.GUI.GUIGlobals;
import com.company.Simulation.SimulationSynchronizerThread;
import com.company.Simulation.SimulationVariables.SimulationGlobals;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class EnvParamForm extends JFrame{
    private JPanel mainPanel;
    private JButton setEnvParamsButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JPanel param1Panel;
    private JPanel param2Panel;
    private JPanel param3Panel;
    private JPanel param4Panel;
    private JLabel statusLabel;
    public JComboBox comboBox1;
    private JButton DeleteButton;

    public EnvParamForm(JLabel mainFrameLabel, SimulationSynchronizerThread ServerThread) {
        this.setTitle("ADCS - Ввод параметров среды");
        this.setSize(GUIGlobals.env_param_frame_width, GUIGlobals.env_param_frame_height);
        this.setVisible(true);

        // позволяет появляться фрейму в центре экрана. (по дефолту - в верхнем левом углу)
        this.setLocationRelativeTo(null);

        // Главная панель со всеми кнопками
        this.add(mainPanel);
        statusLabel.setText("");

        loadComboBox();

        // отображение текущих значений параметров среды
        fillTextFields();

        // Событие нажатия кнопки "задать параметры"
        EnvParamForm thisFrame = this;
        setEnvParamsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox1.getSelectedIndex() == 0) {
                    JDialog jDialog = new AddMaterialDialog(thisFrame);
                }
                submitParams(mainFrameLabel);
            }
        });

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillTextFields(comboBox1.getSelectedIndex());
            }
        });

        DeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBox1.getSelectedIndex();
                if(index!=0) {
                    DeleteMaterialDialog dialog = new DeleteMaterialDialog(thisFrame, index);
                }
            }
        });
    }

    // ввод параметров среды
    public void submitParams(JLabel mainFrameLabel){
        String t1 = textField1.getText();
        String t2 = textField2.getText();
        String t3 = textField3.getText();
        String t4 = textField4.getText();

        // сообщение о корректности ввода
        statusLabel.setText(DataHandler.setEnvParams(t1, t2, t3, t4));
        // если ввод коректный, то на главном фрейме отобразить "Задано" для параметров среды
        if (DataHandler.env_param_input_status) mainFrameLabel.setText("<html><font color='green'>Задано</font></html>");

        if(comboBox1.getSelectedIndex()!=0)
            DBHandler.updateMaterial(
                    comboBox1.getSelectedIndex()-1,
                    DataHandler.lameMu,
                    DataHandler.lameLambda,
                    DataHandler.materialDensity,
                    DataHandler.coefficientNu
            );
    }

    // отображение текущих значений параметров среды в текстовых полях
    public void fillTextFields(){
        textField1.setText(Double.toString(DataHandler.lameMu));
        textField2.setText(Double.toString(DataHandler.lameLambda));
        textField3.setText(Double.toString(DataHandler.materialDensity));
        textField4.setText(String.valueOf(DataHandler.coefficientNu));
    }

    // отображение значений выбранного материала
    public void fillTextFields(int index){
        if(index==0)
            fillTextFields();
        else {
            Material material = DBHandler.materials.get(index - 1);
            textField1.setText(Double.toString(material.lameMu));
            textField2.setText(Double.toString(material.lameLambda));
            textField3.setText(Double.toString(material.materialDensity));
            textField4.setText(String.valueOf(material.coefficientNu));
        }
    }

    // инициализация комбобокса (меню с выбором материала)
    public void loadComboBox(){
        DBHandler.getAllMaterials();
        String[] items = DBHandler.getMaterialNames();
        //comboBox1.removeAllItems();
        // Дефолтный айтем, который можно сохранить как новый материал
        comboBox1.addItem("...");

        for(String item:items)
            comboBox1.addItem(item);
    }
}
