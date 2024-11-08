package com.Midterm;

import com.Midterm.model.Application;
import com.Midterm.model.CustomException;
import com.Midterm.utils.InputDevice;
import com.Midterm.utils.OutputDevice;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws CustomException.InvalidNumberException {
        InputDevice inputDevice = new InputDevice();
        OutputDevice outputDevice = new OutputDevice();
        Application app = new Application(inputDevice, outputDevice);

        if (args.length > 0) {
            // If command-line arguments are provided, use them directly
            app.processArguments(args);
        } else {
            // If no command-line arguments, prompt for input
            try {
                app.run();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        inputDevice.close();
    }
}

// Adjustments needed:
// sa poti sa incepi o sesiune noua fara nici o data salvata sau sa o continui pe cea anterioara
//input verification for username not to have 2 influencers with the same name exception handeling
// do you want to load existing data?
// validation method: all inputs -> dates, names etc1
