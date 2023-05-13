package com.example.geektrust.commands;

import java.util.List;

public interface ICommand {
    void execute(List<String> tokens);
}
