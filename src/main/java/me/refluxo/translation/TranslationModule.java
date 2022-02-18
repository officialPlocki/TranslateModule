package me.refluxo.translation;

import me.refluxo.moduleloader.ModuleLoader;
import me.refluxo.moduleloader.module.Module;
import me.refluxo.moduleloader.module.ModuleManager;
import me.refluxo.moduleloader.module.PluginModule;
import me.refluxo.moduleloader.util.mysql.MySQLService;
import org.bukkit.plugin.Plugin;

@Module(moduleName = "TranslationModule")
public class TranslationModule extends PluginModule {



    @Override
    public void enableModule() {

    }

    @Override
    public void disableModule() {

    }

    @Override
    public ModuleLoader getModuleLoader() {
        return super.getModuleLoader();
    }

    @Override
    public MySQLService getMySQLService() {
        return super.getMySQLService();
    }

    @Override
    public ModuleManager getModuleManager() {
        return super.getModuleManager();
    }

    @Override
    public Plugin getPlugin() {
        return super.getPlugin();
    }
}
