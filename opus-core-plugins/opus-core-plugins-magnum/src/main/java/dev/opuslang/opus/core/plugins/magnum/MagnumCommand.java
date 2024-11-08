package dev.opuslang.opus.core.plugins.magnum;

import dev.opuslang.opus.api.plugin.spi.PluginLoader;
import dev.opuslang.opus.api.service.SubcommandService;
import dev.opuslang.opus.core.plugins.magnum.api.pipeline.PassService;
import dev.opuslang.opus.core.plugins.magnum.pipeline.Pipeline;
import dev.opuslang.opus.core.plugins.magnum.pipeline.PipelineExecutor;
import picocli.CommandLine;

import java.io.File;
import java.util.Arrays;
import java.util.ServiceLoader;

@CommandLine.Command(
        name = "magnum"
)
public class MagnumCommand implements SubcommandService<Void> {

    @CommandLine.Parameters(index = "0")
    private String targetPass;

    @CommandLine.Parameters(index = "1..*")
    private File[] targetFiles = new File[0];

    @CommandLine.Unmatched
    private String[] targetArgs = new String[0];

    @Override
    public Void call() {
        if(this.targetFiles.length <= 0){
            throw new IllegalArgumentException("No files provided.");
        }
        Pipeline.Builder pipelineBuilder = new Pipeline.Builder();
        for(PassService<?> passService : PluginLoader.getInstance().loadServices(ServiceLoader::load, PassService.class)) {
            pipelineBuilder.registerPass(passService);
        }
        Pipeline pipeline = pipelineBuilder.build();
        PipelineExecutor executor = new PipelineExecutor(pipeline, this.targetPass);
        executor.execute(Arrays.stream(this.targetFiles).toList(), this.targetArgs);
        return null;
    }
}
