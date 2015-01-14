# tridiagonal-algorithm-for-relaxed-cubic-spline
Java class to calculate control points for drawing a bézier relaxed spline by Interpolation. 

This class was created to solve a problem on a graphic Java application, where a user inputs 2D coordinates in order to draw a bésier curve. The problem is that to can only create a bézier curve with 4 points, so if you want to join 2 or more curves, you can actually have a problem when gluing the end-point of one curve with the start-point of the other curve.

The solution implemented allows the user to create a spline that interpolates given points in order to create a smooth transition between points given.

