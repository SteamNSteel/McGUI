#Using McGUI
McGUI is not provided as a dependency jar for use in a mod pack. It is distributed as a "deobfuscated" jar, designed with the idea that you will package the code together with your mod, using a technique called Shading. 

The benefit of shading the jar is that if anybody else is using a different version of McGUI, the changes in their version will not break your mod, at the expense that your jar may become a little larger.

#Adding a dependency on McGUI
You can quickly add a depdendency on McGUI using the following gradle snippet

```
dependencies {
    repositories {
        maven {
            url 'http://repository.steamnsteel.info/artifactory/steamnsteel-libs-unstable'
        }
    }
    compile(group: 'mod.steamnsteel', name: 'mcgui', version: '1.0', classifier: 'deobf')
}
```

after updating your IDE, you should be able to start coding with McGUI.

#Shading McGUI with Gradle
Start by looking for the buildscript { } section of your gradle file.

You will make sure you have a repositories { } subsection that contains jcenter(). It may also contain other repositories like Forge and others.

In the dependencies { } subsection, add a classpath to 'com.github.jengelman.gradle.plugins:shadow:1.2.2'

It should look something like the following:

```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.github.jengelman.gradle.plugins:shadow:1.2.2'
    }
}
```
 
 After this, section, you can apply the plugin 'com.github.johnrengelman.shadow'
 
 ```
...
        classpath 'com.github.jengelman.gradle.plugins:shadow:1.2.2'
    }
}

apply plugin: 'com.github.johnrengelman.shadow'
...
```

After you have applied the plugin, you should check to see if the jar for your mod being built has a classifier.
It will look something like this

```
jar {
    classifier = 'universal'
}
```

take note of the classifier, you'll need it in a moment.

Before we make the magic happen, I'll explain what this is going to do to your output.

1. Your normal Jar file will be built.
2. The shadowJar task will run, it will copy all the files out of McGUI, and embed them inside your jar.
3. The packages of McGUI will be rewritten to what you specify
4. Your compiled class files will be rewritten to use the adjusted package names.
5. The resulting shaded jar will replace the normal jar
6. ForgeGradle's reobf task will read the shaded jar (because it was overwritten) and obfuscate everything, including McGUI.

Now, let's make the shading happen. I'll show you the section, and then I'll explain it.

```
shadowJar {
    dependencies {
        include(dependency('mod.steamnsteel:mcgui:1.0:deobf'))
    }
    relocate 'mod.steamnsteel.mcgui', 'mod.steamnsteel.shaded.mod.steamnsteel.mcgui'
    classifier = 'universal'
}

reobf.jar.task.dependsOn shadowJar
```

The 'shadowJar' task is provided by the plugin, it is the task that will embed one jar within another, and optionally change all the package references to prevent colliding.

Inside of the dependencies, you specify which artifacts you are going to shade. In this case, the deobfuscated McGUI. You may add other dependencies to shade here too.

Next, we've annouced that the classifier of the shadowJar will be the same as the unshaded jar, so that the reobfuscation task will work on the shaded jar.

Lastly, we tell ForgeGradle that it's reobfuscation task (reobf) depends on having the shadowJar task run first.

You should be able to run your mod both inside your IDE and in production without the McGUI jar being present.

#Shading shortcut
If you don't care about how Shading works, you can paste the following snippet at the bottom of your build.gradle to get McGUI shading for free.
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.github.jengelman.gradle.plugins:shadow:1.2.2'
    }
}

apply plugin: 'com.github.johnrengelman.shadow'

shadowJar {
    dependencies {
        include(dependency('mod.steamnsteel:mcgui:1.0:deobf'))
    }
    relocate 'mod.steamnsteel.mcgui', 'mod.steamnsteel.shaded.mod.steamnsteel.mcgui'
    classifier = 'universal'
}

reobf.jar.task.dependsOn shadowJar
```

