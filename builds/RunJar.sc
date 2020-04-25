import $ivy.`com.lihaoyi::mill-contrib-playlib:$MILL_VERSION`
import mill.playlib.Static

trait RunJar extends Static {
  // to make it runnable and be consistent with assembly output
  // 1) In app source code, put all js/css files in : [[millSourcePath / assetsPath()]], it is app/public
  // 2) create a runPublicAssets Task that copy all files in staticAssets and webJarResources to "T.dest/../.." folder
  // 3) call runPublicAssets from forkWorkingDir and set it to "T.dest/../../
  // 4) in code, resolve the statci assets to public folder, webjars in public/lib folder. This is consistent to the assembly output

  def runWorkingDir = T { T.dest / os.up / os.up }

  def runPublicAssets() = T.command {
    val runPublic = runWorkingDir() / assetsPath()

    val publicStatic = staticAssets().path / assetsPath()
    if (os.exists(publicStatic)) {
      os.list(publicStatic)
        .map(path => os.copy.over(path, runPublic / path.last))
    }

    val webJarPublic = webJarResources().path / assetsPath()
    if (os.exists(webJarPublic))
      os.list(webJarPublic)
        .map(path => os.copy.over(path, runPublic / path.last))

    PathRef(T.dest)
  }

  override def forkWorkingDir = T {
    runPublicAssets()
    runWorkingDir()
  }
}
