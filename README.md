# What's this?

With this project, you can put together audio albums. The files can be selected from a Swing UI
and are moved and renamed appropriately after you are finished with your album. This way you
can re-assemble albums from your MP3 collection after the metadata got lost.

# Build using Earthly

The CI build of this project uses [Earthly](https://docs.earthly.dev/), which in turn uses
container virtualization (e.g. Docker or Podman). You can also run the build locally (if you
have Earthly as well as an OCI compatible container engine installed) by executing
`earthly +build`. This will create a container with everything needed for the build,
create the package inside it and then copy the results to the directory `target` for you.
