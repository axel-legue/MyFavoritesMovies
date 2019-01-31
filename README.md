# MyFavoritesMovies

## Project Overview
In this project, i create an application that allow users to discover 
the most popular / highest rated movies playing.
The users will have access to trailers and review, and the possibility to add them in favorite.

## Contains
Through this project, i use:
- RecyclerView with gridLayout adapter.
- CardView
- Implicit / Explicit Intent
- Retrofit 
- RxAndroid
- Picasso
- ButterKnife
- Constraint Layout
- Learn how to use MediaPlayer/ExoPlayer t display videos
- Handle Error cases in Android
- Shared Element Transition
- Architecture Component (Room / LiveData / ViewModel)

## Look
- Home Screen <br/>
![Image](https://github.com/axel-legue/MyFavoritesMovies/blob/master/Home_screen.png =250x250) <br/>
- Detail Screen <br/>
![Image](https://github.com/axel-legue/MyFavoritesMovies/blob/master/detail_movie.png =250x250)  <br/>
- Detail Screen - Trailer Review <br/>
![Image](https://github.com/axel-legue/MyFavoritesMovies/blob/master/review_trailer.png =250x250) <br/>

## Usefull Resources 

 - Shared element transition : <br/>
http://mikescamell.com/shared-element-transitions-part-1/ <br/>
https://android-developers.googleblog.com/2018/02/continuous-shared-element-transitions.html <br/>

- Executor : <br/>
https://developer.android.com/reference/java/util/concurrent/Executor  <br/>

- NetworkBoundResource : <br/>
https://github.com/googlesamples/android-architecture-components/blob/b1a194c1ae267258cd002e2e1c102df7180be473/GithubBrowserSample/app/src/main/java/com/android/example/github/repository/NetworkBoundResource.java <br/>

- Double-check locking : <br/>
https://en.wikipedia.org/wiki/Double-checked_locking

- Save state RecyclerView : <br/>
https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state


# Note for instructor
You need to put you API KEY in 2 places :
    -MainActivity inside variable named "API_KEY_VALUE"
and in
    -DetailMovieActivity inside variable "API_KEY_VALUE"

