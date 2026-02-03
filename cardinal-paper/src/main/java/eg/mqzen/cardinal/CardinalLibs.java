package eg.mqzen.cardinal;

import com.alessiodp.libby.Library;
import com.alessiodp.libby.Repositories;

public interface CardinalLibs {
    Library IMPERAT_CORE = Library.builder()
                                   .groupId("studio{}mevera")
                                   .artifactId("imperat-core")
                                   .version("2.4.2")
                                   .resolveTransitiveDependencies(true)
                                   .relocate("studio{}mevera", "eg{}mqzen{}cardinal{}libs{}studio{}mevera")
                                   .excludeTransitiveDependency("net{}kyori", "adventure-text-minimessage")
                                   .excludeTransitiveDependency("net{}kyori", "adventure-platform-bukkit")
                                   .build();

    Library IMPERAT_VELOCITY = Library.builder()
                                       .repository(Repositories.MAVEN_CENTRAL)
                                       .groupId("studio{}mevera")
                                       .artifactId("imperat-velocity")
                                       .version("2.4.2")
                                       .resolveTransitiveDependencies(true)
                                       .relocate("studio{}mevera", "eg{}mqzen{}cardinal{}libs{}studio{}mevera") //eg.mqzen.guilds.libs.dev.velix
                                       .relocate("net{}kyori", "eg{}mqzen{}guilds{}libs{}net{}kyori")
                                       .excludeTransitiveDependency("net{}kyori", "adventure-text-minimessage")
                                       .excludeTransitiveDependency("net{}kyori", "adventure-platform-bukkit")
                                       .build();

    Library MONGODB_DRIVER = Library.builder()
                                     .repository(Repositories.MAVEN_CENTRAL)
                                     .groupId("org{}mongodb")
                                     .artifactId("mongodb-driver-sync")
                                     .version("5.3.1")
                                     .resolveTransitiveDependencies(true)
                                     .relocate("com{}mongodb", "eg{}mqzen{}cardinal{}libs{}com{}mongodb")
                                     .relocate("org{}bson", "eg{}mqzen{}guilds{}libs{}org{}bson")
                                     .build();

    Library[] ALL = new Library[] {
        IMPERAT_CORE,
        IMPERAT_VELOCITY,
        MONGODB_DRIVER
    };

}
