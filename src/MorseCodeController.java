import javax.sound.sampled.*;
import java.util.HashMap;



//this class will handle the logic for our gui
public class MorseCodeController {

    //we will use hashmap to translate user inout to morse code
    //a hashmap is a data structure which stores key/value pairs
    //in this case , we will use the letter as the key and the corresponding morse code as the value
    //this way we can easily look up the morse code for any given letter by using the lettr as the key

    //here i am declaring a hashmap to have a key of type "character" with a value of type "string"
    private HashMap<Character, String> morseCodeMap;

    public MorseCodeController() {
        morseCodeMap = new HashMap<>();

        //populating the hashmap with the letters and their corresponding morse code
        morseCodeMap.put('A', ".-");
        morseCodeMap.put('B', "-...");
        morseCodeMap.put('C', "-.-.");
        morseCodeMap.put('D', "-..");
        morseCodeMap.put('E', ".");
        morseCodeMap.put('F', "..-.");
        morseCodeMap.put('G', "--.");
        morseCodeMap.put('H', "....");
        morseCodeMap.put('I', "..");
        morseCodeMap.put('J', ".---");
        morseCodeMap.put('K', "-.-");
        morseCodeMap.put('L', ".-..");
        morseCodeMap.put('M', "--");
        morseCodeMap.put('N', "-.");
        morseCodeMap.put('O', "---");
        morseCodeMap.put('P', ".--.");
        morseCodeMap.put('Q', "--.-");
        morseCodeMap.put('R', ".-.");
        morseCodeMap.put('S', "...");
        morseCodeMap.put('T', "-");
        morseCodeMap.put('U', "..-");
        morseCodeMap.put('V', "...-");
        morseCodeMap.put('W', ".--");
        morseCodeMap.put('X', "-..-");
        morseCodeMap.put('Y', "-.--");
        morseCodeMap.put('Z', "--..");

        //populating the hashmap with symbols and their corresponding morse code
        morseCodeMap.put(' ', " "); //space between words
        morseCodeMap.put('.', ".-.-.-");
        morseCodeMap.put(',', "--..--");
        morseCodeMap.put('?', "..--..");
        morseCodeMap.put('!', "-.-.--");
        morseCodeMap.put('@', ".--.-.");
        morseCodeMap.put('&', ".-...");
        morseCodeMap.put(':', "---...");
        morseCodeMap.put(';', "-.-.-.");
        morseCodeMap.put('=', "-...-");
        morseCodeMap.put('+', ".-.-.");
        morseCodeMap.put('-', "-....-");
        morseCodeMap.put('_', "..--.-");
        morseCodeMap.put('*', "-..-");
        morseCodeMap.put('(', "-.--.");
        morseCodeMap.put(')', "-.--.-");
        morseCodeMap.put('/', "-..-.");
        morseCodeMap.put('\\', "-..-.");
        morseCodeMap.put('"', ".-..-.");
        morseCodeMap.put('\'', ".----.");
        morseCodeMap.put('$', "...-..-");
        morseCodeMap.put('%', "-----..");


        //populating the hashmap with numbers and their corresponding morse code
        morseCodeMap.put('0', "-----");
        morseCodeMap.put('1', ".----");
        morseCodeMap.put('2', "..---");
        morseCodeMap.put('3', "...--");
        morseCodeMap.put('4', "....-");
        morseCodeMap.put('5', ".....");
        morseCodeMap.put('6', "-....");
        morseCodeMap.put('7', "--...");
        morseCodeMap.put('8', "---..");
        morseCodeMap.put('9', "----.");


    }

    public String translateToMorse(String textToTranslate) {
        StringBuilder translatedText = new StringBuilder();
        textToTranslate = textToTranslate.toUpperCase();
        for (Character letter : textToTranslate.toCharArray()) {


            //translate the letter and then append to the returning value (to be displayed to the GUI)
            translatedText.append(morseCodeMap.get(letter) + " ");
        }
        return translatedText.toString();
    }

    //morseMessage - contains the morse message after being translated
    public void playSound(String[] morseMessage) throws LineUnavailableException, InterruptedException {

        //audioFormat specifies the audio properties i.e. the type of sudio we want
        AudioFormat audioFormat = new AudioFormat(44100, 8, 1, true, false);

        //create the dataline to play the incoming audio data
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        //duration of the amount to be played ( i just messed around with the values to get it close enough)
        int dotDuration =150; //duration of a dot in milliseconds
        int dashDuration = 3* dotDuration; //duration of a dash in milliseconds
        int slashDuration = 7 * dashDuration; //duration of a slash in milliseconds


        for (String pattern : morseMessage) {
            // play the letter sound
            for (char c : pattern.toCharArray()) {
                if (c == '.') {
                    playBeep(sourceDataLine, dotDuration);
                    Thread.sleep(dotDuration); // pause between symbols

                } else if (c == '-') {
                    playBeep(sourceDataLine, dashDuration);
                    Thread.sleep(dotDuration); // pause between symbols

                } else if (c == '/') {
                    playBeep(sourceDataLine, slashDuration);

                }
            }
            //waits a bit before playing the next sequence
                Thread.sleep(dotDuration);
        }

        //close audio output line [ cleans up resources]
        sourceDataLine.drain();
        sourceDataLine.stop();
        sourceDataLine.close();
    }

    //sends audio data to be played to the data line
    private void playBeep(SourceDataLine line, int duration) {

            float sampleRate = 44100;
            int numSamples = (int) ((duration / 1000.0) * sampleRate);
            byte[] data = new byte[numSamples];

            double frequency = 800; // Morse tone freq (clearer than 440)

            for (int i = 0; i < numSamples; i++) {
                double angle = 2.0 * Math.PI * i * frequency / sampleRate;
                data[i] = (byte) (Math.sin(angle) * 127);
            }

            line.write(data, 0, data.length);

//        //create audio data
//        byte[] data = new byte[duration * 44100 / 1000];
//
//        for (int i = 0; i < data.length; i++) {
//            //calculates the angle of sine wave for the current sample based on sample rate and frequency
//            double angle = i / (44100 / 440) * 2 * Math.PI;
//
//            //calculates the amplitude of the sine wave at current angle and
//            // scale it to fit within a range of -128 to 127
//            // also in the context of audio processing , a signed bytes is often used to
//            // represent audio data because it can represent both positive
//            // and negative amplitudes of sound waves
//            data[i] = (byte) (Math.sin(angle) * 127);
//        }
//
//        //write the audio data in data line to be played
//        line.write(data, 0, data.length);


    }
}


