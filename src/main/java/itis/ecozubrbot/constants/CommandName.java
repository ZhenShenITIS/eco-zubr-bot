package itis.ecozubrbot.constants;

public enum CommandName {

    START("/start"),
    UNKNOWN("/"),
    HELP("/help");


    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
