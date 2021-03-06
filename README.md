
# OpenTOSCA Container - TOSCA Runtime

[![Build Status](https://travis-ci.org/OpenTOSCA/container.svg?branch=master)](https://travis-ci.org/OpenTOSCA/container)

Part of the [OpenTOSCA Ecosystem](http://www.opentosca.org)


## Build

1. Run `mvn package` inside the root folder.
2. When completed, the built product can be found in `org.opentosca.container.product/target/products`.


## Setup in Eclipse

- After checkout, import the project to Eclipse (on the root directory) and select all found projects.
  - File > Import... > Maven > Existing Maven Projects > Next
  - Select appropriate Root Directory
  - Select all projects
  - OK
- When Eclipse asks to install the Tycho Configurators, hit Yes/Okay/Install (be sure that `m2e` and it's repositories are known to your Eclipse).
- Then, in the (sub-)project `target-definition` open the file `target-definition.target` and click `Set as Target Platform` (top right).
- To start the container, in (sub-)project `org.opentosca.container.product` open the `*.product` file and run the application.
