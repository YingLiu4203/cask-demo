# cask-demo

A demo app using cask based on Haoyi's blog: [Working with Databases using Scala and Quill](http://www.lihaoyi.com/post/WorkingwithDatabasesusingScalaandQuill.html). Major changes are

- use latest versions of dependencies
- use separate route files
- fix `mill app.run`

## Separation of Concerns

The application is broken into to build modules: the `app` is a a typical web application that servers web pages and API requests, the `appJs` is a Scalajs proejct that contains all client Js code.

The `appJs` depenss on the `app` to share common data models. However, the `appJs` is required to run the `app` properly. There are two ways to organize the build modules:

- nest `appJs` in `app`. It requires that the `app.run` can depend on `app.appJs.fastOpt`. This one only works if `app` only use Scalajs-compatible libraries.
- Add a third module that depends on both `appJs` and `app`. Its purpose is for assembly or run. This works fine.
