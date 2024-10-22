package dev.opuslang.opus.api.plugin.service;

import java.util.concurrent.Callable;

public interface PluginCommandService<T> extends Callable<T> {
}
