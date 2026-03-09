import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//keylistener is used here so that i can listen to the key presses(typing)
public class MorseCodeTranslatorGUI extends JFrame implements KeyListener {
        private MorseCodeController morseCodeController;
        //textInputArea- user input (text to be translated)
        // morseCodeTranslator- translated text nto morse code
        private JTextArea textInputArea, morseCodeArea;

        public MorseCodeTranslatorGUI() {

            //Basically adds text to the title bar
            super("Morse Code Translator");

            // sets the size of frame to be 540x760 pixels
            setSize(new Dimension(540, 760));

            //prevents GUI from being able to be resized
            setResizable(false);

            //setting the layout to be null allows us to manually position and set the size of the components in our GUI
            setLayout(null);

            //exits program when closing the GUI
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //changes the color of background
            getContentPane().setBackground(Color.decode("#264563"));

            //places the GUI on center of the screen when ran
            setLocationRelativeTo(null);

            morseCodeController = new MorseCodeController();
            addGUIComponents();


        }

        private void addGUIComponents() {
            //title label
            JLabel titleLabel = new JLabel("Morse Code Translator");

            //change the font size and font weigth of the title label
            titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));

            //CHANGES THE FOONT COLOR
            titleLabel.setForeground(Color.WHITE);

            //CENTERS TEXT (RELATIVE TO ITS CONTAINER WEIGHT)
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

            //sets the x,y position an dwidth and height dimension
            //to make sure that the title aligns to the center of our GUI , we made it the same width
            titleLabel.setBounds(0, 20, 540, 100);

           // text input
            JLabel textInputLabel = new JLabel("Hello Akriti, Enter text:");
            textInputLabel.setFont((new Font("Dialog", Font.PLAIN, 18)));
            textInputLabel.setForeground(Color.WHITE);
            textInputLabel.setBounds(20, 100, 200, 30);

            textInputArea = new JTextArea();
            textInputLabel.setFont(new Font("Dialog", Font.PLAIN, 16));

            //makes it so that we are listening to the key presses whenever we aare typing in this text area
            textInputArea.addKeyListener(this);

            //simulates padding of 10px in the text area
            textInputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            //makes it so that words wrap to the next line after reaching the end of text area
            textInputArea.setLineWrap(true);

            // makes it so that when do get wrap , the word doesnot get split
            textInputArea.setWrapStyleWord(true);

            //adds scroling ability to the input text area
            JScrollPane textInputScroll = new JScrollPane(textInputArea);
            textInputScroll.setBounds(20, 132, 484, 136);

            //morse code input area
            JLabel morseCodeInputLabel = new JLabel("Morse Code ");
            morseCodeInputLabel.setFont((new Font("Dialog", Font.PLAIN, 18)));
            morseCodeInputLabel.setForeground(Color.WHITE);
            morseCodeInputLabel.setBounds(20, 390, 200, 30);

            morseCodeArea = new JTextArea();
            morseCodeArea.setFont(new Font("Dialog", Font.PLAIN, 18));
            morseCodeArea.setEditable(false);
            morseCodeArea.setLineWrap(true);
            morseCodeArea.setWrapStyleWord(true);
            morseCodeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            //adds scroling ability to the morse code input text area
            JScrollPane morseCodeScroll = new JScrollPane(morseCodeArea);
            morseCodeScroll.setBounds(20, 430, 484, 236);

            //play sound button
            JButton playSoundButton = new JButton("play sound");
            playSoundButton.setBounds(210, 680, 100, 30);
            playSoundButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){

                    //disable the play botton (prevents the sound from getting interrupted)
                    playSoundButton.setEnabled(false);

                    Thread playMorseCodeThread = new Thread(new Runnable() {
                        @Override
                        public void run(){
                            //attempt to play th emorse code sound
                            try {
                                String[] morseCodeMessage = morseCodeArea.getText().split(" ");
                                morseCodeController.playSound(morseCodeMessage);
                            }catch(LineUnavailableException lineUnavailableException){
                                lineUnavailableException.printStackTrace();
                            }catch(InterruptedException interruptedException){
                                interruptedException.printStackTrace();
                            }finally {
                                //re-enable the play button after sound has finished playing
                                playSoundButton.setEnabled(true);
                            }
                        }
                    });
                    playMorseCodeThread.start();
            }
        });

            //add to GUI
            add(titleLabel);
            add(textInputLabel);
            add(textInputScroll);
            add(morseCodeInputLabel);
            add(morseCodeScroll);
            add(playSoundButton);

        }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

            //ignore shift key press
            if (e.getKeyCode() != KeyEvent.VK_SHIFT){

                //retrieve text input
                String inputText = textInputArea.getText();

                //update the GUI with the translated text
                morseCodeArea.setText(morseCodeController.translateToMorse(inputText));

            }
    }
}

