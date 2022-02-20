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

    /**
     * Returns the module loader for this module
     *
     * @return The ModuleLoader that is associated with the current class loader.
     */
    @Override
    public ModuleLoader getModuleLoader() {
        return super.getModuleLoader();
    }

    /**
     * Returns the MySQLService object
     *
     * @return The MySQLService object.
     */
    @Override
    public MySQLService getMySQLService() {
        return super.getMySQLService();
    }

    /**
     * Returns the module manager
     *
     * @return The ModuleManager instance that is associated with the current application.
     */
    @Override
    public ModuleManager getModuleManager() {
        return super.getModuleManager();
    }

    /**
     * Returns the plugin that this class is a part of
     *
     * @return The plugin that is being used.
     */
    @Override
    public Plugin getPlugin() {
        return super.getPlugin();
    }
}
