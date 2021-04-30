import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class Gui implements ActionListener {
    private static JLabel texto;
    private static JLabel output;
    private static JTextField input;
    private static JButton traduzir;
    private static JButton resetar;
    static MiniTeste mt = new MiniTeste();

    public void janela() {

        JFrame frame = new JFrame("Tradutor (Lingua Portuguesa -> Lógica de Predicado)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 130);

        JPanel panelInput = new JPanel();
        JPanel panelOutput = new JPanel();

        texto = new JLabel("Texto:");
        input = new JTextField(25); // accepts upto 10 characters
        traduzir = new JButton("Traduzir");
        resetar = new JButton("Resetar");
        output = new JLabel("");

        traduzir.addActionListener(this);
        resetar.addActionListener(this);
        panelInput.add(texto);
        panelInput.add(input);
        panelInput.add(traduzir);
        panelInput.add(resetar);

        panelOutput.add(output);

        frame.getContentPane().add(BorderLayout.NORTH, panelInput);
        frame.getContentPane().add(BorderLayout.CENTER, panelOutput);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == traduzir){
            ArrayList<String> traducao = mt.tradutorLP(input.getText());
            output.setText(traducao.toString().replace("[","").replace("]","").replace(","," "));
        }else if (e.getSource() == resetar){
            input.setText("");
            output.setText("");
        }
    }

    public static void main(String[] args) {
        Gui gui = new Gui();
        gui.janela();
    }
}