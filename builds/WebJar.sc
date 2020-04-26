import $ivy.`com.lihaoyi::mill-contrib-playlib:$MILL_VERSION`
import mill.playlib.Static

trait RunWebJar extends Static {
  // to make it runnable and be consistent with assembly output
  // 1) In app source code, put all js/css files in : [[millSourcePath / assetsPath()]], it is app/public
  // 2) create a runPublicAssets Task that copy all files in staticAssets and webJarResources to "T.dest/../.." folder
  // 3) call runPublicAssets from forkWorkingDir and set it to "T.dest/../../
  // 4) in code, resolve the statci assets to public folder, webjars in public/lib folder. This is consistent to the assembly output

  def runPublicAssets = T {
    val runWorkingDir = T.dest / os.up / os.up
    val runPublic = runWorkingDir / assetsPath()
    if (os.exists(runPublic)) os.remove.all(runPublic)
    os.makeDir.all(runPublic)

    // don't use os.copy.over, raise an error if there is a conflict
    val publicStatic = staticAssets().path / assetsPath()
    if (os.exists(publicStatic)) {
      os.list(publicStatic)
        .map(path => os.copy(path, runPublic / path.last))
    }

    val webJarPublic = webJarResources().path / assetsPath()
    if (os.exists(webJarPublic))
      os.list(webJarPublic)
        .map(path => os.copy(path, runPublic / path.last))

    PathRef(runPublic)
  }

  override def forkWorkingDir = T {
    runPublicAssets()
    T.dest / os.up / os.up
  }
}
