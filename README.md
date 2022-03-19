# JadAPI

JadAPI is a compatibility API with the idea to bring 1.7 to 1.18 support and/or add functionality that BukkitAPI will never have!
It also contains some Objects mapping in NMS to provide this compatibility and/or functionality.

## How to use JadAPI?

First things first, add JadAPI to your plugin's dependencies on your plugin.yml like this:
```yaml
depend: [ JadAPI ]
```
After this, make sure you add JadAPI to your project.

## Using the API
To use the api, you firstly need to create a JadAPIPlugin instance by extending it!
```java
public class ExampleAPIInstance extends JadAPIPlugin {
  
  private final JavaPlugin plugin;
  
  public ExampleAPIInstance(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  
  @Override
  public JavaPlugin getJavaPlugin() {
    return this.plugin;
  }
}
```
after creating such JadAPI instance, you register it in your Main class! (Instances can only be enabled onEnable)
```java
public class Main extends JavaPlugin {
    
  public void onEnable() {
    new ExampleAPIInstance(this).register(true);
  }
  
}
```
Saving the instance is not needed as you can get it by using: `JadAPIPlugin.get(Class<JavaPlugin> mainClass)`

## What is the use of the instance?
With this instance, you can create Quick Listeners (listen for a bukkit event), Packet hooks (listen for packets from a specific player or all), Enchantments that use the BukkitAPI, or craftings that also use the bukkit api! (mostly for compatibility due to the NamespacedKey changes and stuff!
but there's way more then this!

## Some of the classes that are more interesting!
`JInventory`

`JItemStack`

`JEnchantmentInfo`

`JScoreboard`

`JPlayer`

`JEntity`

`JMaterial`

`JHeadStorage` (Some heads are pre-added by JadAPI, check `JadAPI.class`!)

### Packages that I think are quite useful.
Consider checking dev.jadss.jadapi.management.labymod where the package is specific to labymod packet sending and stuff like this! It's very useful for managing labymod clients.
Consider also checking https://docs.labymod.net/pages/server/introduction/ for it's documentation if you find difficulties with any of the names of the classes, etc.


## NMS

JadAPI also comes with some bundled NMS api utilities! They are very good if you want to do some custom stuff, like holograms or editing signs! I absolutely think this is necessary for some support between versions, and I will continue updating them as time goes on!
