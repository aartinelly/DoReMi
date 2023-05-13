package com.example.geektrust;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


import com.example.geektrust.appConfig.ApplicationConfig;
import com.example.geektrust.commands.CommandInvoker;
import com.example.geektrust.exceptions.NoSuchCommandException;

public class Main {
    public static void main(String[] args)  {
		// args = new String[]{"sample_input/input2.txt"};
        ApplicationConfig applicationConfig = new ApplicationConfig();
		CommandInvoker commandInvoker = applicationConfig.getCommandInvoker();

		BufferedReader reader;
		int i=0;
		String fileName = args[i];
		try{
			reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();

			while(line != null){
				List<String> tokens = Arrays.asList(line.split(" "));
				
				commandInvoker.execute(tokens.get(i), tokens);

				line = reader.readLine();
			}
			reader.close();
		}
		catch(IOException | NoSuchCommandException e){
			e.printStackTrace();
		}
        
	}
}
