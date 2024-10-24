package dev.opuslang.opus.core;

import dev.opuslang.opus.api.plugin.service.PluginCommandService;
import dev.opuslang.opus.core.cli.OpusCommand;
import dev.opuslang.opus.core.plugin.PluginLoader;
import picocli.CommandLine;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        PluginLoader.getInstance().loadPlugins("plugins/");
        CommandLine cli = new CommandLine(new OpusCommand());
        for(PluginCommandService<?> commandService : PluginLoader.getInstance().loadServices(ServiceLoader::load, PluginCommandService.class)){
            cli.addSubcommand(commandService);
        }

        if(Arrays.equals(args, new String[]{"!interactiveargs"})){
            Scanner scanner = new Scanner(System.in);
            String nextArg = "";
            List<String> newArgs = new ArrayList<>();
            while(!(nextArg = scanner.next()).equals("!")){
                newArgs.add(nextArg);
            }
            cli.execute(newArgs.toArray(String[]::new));
        }else{
            cli.execute(args);
        }

    }
}