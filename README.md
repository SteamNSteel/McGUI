#Using McGUI
McGUI is designed with the idea that you will package the code together with your mod, using a technique called Shading. 

THe benefit of shading the jar is that if anybody else is using a different version of McGUI, the changes in their version will not break your mod.

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

take note of the classifier 