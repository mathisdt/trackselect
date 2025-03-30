# What's this?

With this project, you can extract parts from a video medium (DVD or BluRay) using a GUI.
In the background, command-line utilities like `lsdvd` and `HandBrakeCLI` do the real work.

# Build using Earthly

The CI build of this project uses [Earthly](https://docs.earthly.dev/), which in turn uses
container virtualization (e.g. Docker or Podman). You can also run the build locally (if you
have Earthly as well as an OCI compatible container engine installed) by executing
`earthly +build`. This will create a container with everything needed for the build,
create the package inside it and then copy the results to the directory `target` for you.
