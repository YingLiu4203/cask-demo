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

## Try of Scalajs

The `scalajs-webjar` branch is a try of putting all js code into Scalajs project. The motivations are:

- Strong typing
- Server and client can share some Scala model data
- Rich Scala functions

However, the try proved that it is not a good fit for server-side rendering (SSR) project. The reasons are:

- Js is essentially dynamic-typed and everything received is untyped. No matter what tools used, one has to maitain all typing info and the transformation is still dynamic.
- To share data model, one has to create a separate shared build module for the sharing. To make a change, one has to change three places instead of two.
- Js function are good enough.
- You have to double the learning effort: one for the Js libs and one for using Js libs in Scala.
- Managing two or three build modules are really painful.
- It is very hard to debug generated Js code.
- Much more Scala code is required than the Js code. In an Ajax call, 50 lines of Scala code vs. 15 lines of Js code.
- Maning NPM packages is not easy.
- Scalajs libs are not mature. The ajax call functions of udash-jquery 3.0.4 don't work.

The conclusion is that one should avoid Scalajs in SSR project. For SPA, one can give it a try. It is a one-week try effort. I learned a lot about the build tool `mill`.
