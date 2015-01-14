package splines;

import java.awt.geom.Point2D;
import java.util.Vector;

/**
 * Java class for the mathematical treatment for drawing relaxed cubic splines by points interpolation
 * @author Sérgio Faustino
 * @see http://en.wikipedia.org/wiki/Tridiagonal_matrix_algorithm
 * @see http://www.math.ucla.edu/~baker/149.1.02w/handouts/dd_splines.pdf
 */
public class TridAlgoritm {
	
	//Class variables
	private int size;
	private Vector<Double> x_cord= new Vector<Double>();
	private Vector<Double> y_cord= new Vector<Double>();
	private Vector<Point2D.Double> data;

	
	
	/**
	 * Construtor
	 * @param points - vetor de pontos recolhidos<Point2D.Double>
	 * @param points - vector of <Point2D.Double> for treatment
	 */
	public TridAlgoritm(Vector<Point2D.Double> points){
		this.data= points;	
		this.size= points.size();
		
		//populate the vectors x and y
		for (int i=0; i< points.size(); i++ ){
			x_cord.addElement(points.get(i).x);
			y_cord.addElement(points.get(i).y);
		}
		
	}

	/**
	 * Method that returns the size of the vector 
	 * @return Size of vector(Points)
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Method that returns the x axis coordinates
	 * @return Vector of x coordinates
	 */
	public Vector<Double> getX_cord() {
		
		return x_cord;
	}
	
	/**
	 * Method that sets the x axis coordinates
	 * @param x_cord - Vector of coordinates
	 */
	public void setX_cord(Vector<Double> x_cord) {

		this.x_cord = x_cord;
	}

	/**
	 * Method that returns the y axis coordinates
	 * @return Vector of y coordinates
	 */
	public Vector<Double> getY_cord() {
		
		return y_cord;
	}

	/**
	 * Method that sets the y axis coordinates
	 * @param y_cord - Vector of coordinates
	 */
	public void setY_cord(Vector<Double> y_cord) {
		this.y_cord = y_cord;
	}
	
	/**
	 * PT
	 * Este metodo vai criar o vetor de coordenadas necessários para
	 * implementar no cáculo da matriz tridiagonal, de acordo com a fórmula matemática
	 * definida no cálculo da relaxed cubic splines
	 * [ (6S(1) - S0)  ]
	 * [ (6S) if i>1   ]
	 * [ (6S(n-1) - Sn)]
	 * devolvendo um vetor de cordenadas transformadas	
	 */
	/**
	 * EN
	 * This method will create a vector from a coordinate required to calculate the interpolation of the 
	 * relaxed spline used on the matrix operations.
	 * [ (6S(1) - S0)  ]
	 * [ (6S) if i>1   ]
	 * [ (6S(n-1) - Sn)]
	 */
	private Vector<Double> setVector(Vector<Double> vector){
		Vector<Double> theVector = new Vector<Double>();
		
		theVector.addElement((6*vector.get(1)- vector.get(0)));
		
		for (int i=2; i< vector.size()-2; i++ )
				theVector.addElement(6*vector.get(i));
		
		theVector.addElement((6*vector.get(vector.size()-2)- vector.get(vector.size()-1)));
		
		return theVector;
	}
		
	/**
	 * PT
	 * Este método efectua a execução do algoritmo Tridiagonal para cada cada cordenada,
	 * necessário para o cálculo dos pontos de controlo para a relaxed b-splines 
	 * definindo os valores da matriz identidade para b=4 and a=c=1
	 * | b0 c0  0 |
	 * | a0 b1 c1 |
	 * | 0  a1 b2 |
	 * EN
	 * This method will execute the Tridiagonal Algorithm for each coordinate using the 
	 * vector of interpoladed coordinates to calculate the position of the control points
	 * from each point. 
	 */
	private Vector<Double> Tridiagonal(Vector<Double> vector){

		double [] a = new double[data.size()-2]; 
		double [] b = new double[data.size()-2];
		double [] c = new double[data.size()-2];
		double [] d = new double[data.size()-2];
		Vector<Double> dVector = new Vector<Double>();
		//we want to keep the origin and end point out of the equation
		//so it is needed to remove those spaces from the array
		int n=data.size()-3; 
		
		//setting the S vector
		dVector = setVector(vector);

		//loop from 1 to i which will be the length of the vector
		for (int i=0; i<data.size()-2; i++){
			d[i]=dVector.get(i);
			a[i]=1;
			b[i]=4;
			c[i]=1;
		}
		a[0]=0;
		c[data.size()-3]=0; 
		
		c[0] /= b[0];
	    d[0] /= b[0];
	    
		//1st iteration
		for (int i=1; i<n; i++){
			c[i] /= b[i] - a[i]*c[i-1];
	        d[i] = (d[i] - a[i]*d[i-1]) / (b[i] - a[i]*c[i-1]);
			
		}
		//2nd interaction
		d[n] = (d[n] - a[n]*d[n-1]) / (b[n] - a[n]*c[n-1]);
	
		 for (int i = n; i-- > 0;) {
		        d[i] -= c[i]*d[i+1];
		    }
		//will reuse the dVector to store the vector 
		for (int i=0; i<data.size()-2; i++)
			dVector.set(i, d[i]);
		
		return dVector;
	}
	
	/**
	 * PT
	 * Este método separa as cordenadas para calculo pelo algoritmo, voltando
	 * a agrupar em um vetor de pontos já devidamente modificados para uma curva do tipo
	 * relaxed cubic splines, mantendo a posição do 1º e ultimo ponto do grupo
	 * @return Vector de pontos de controlo para desenho da forma
	 * EN
	 * This method gathers the X and Y coordinates given by the user, calculates the draw points 
	 * for the bézier using the interpolation of relaxed b-splines and
	 * transforming it using the Tridiagonal Algorithm, and returns a vector with all points for drawing.
	 */
	public Vector<Point2D.Double> controlPoints (){
		Vector<Point2D.Double> temp = new Vector<Point2D.Double>();
		Vector<Double> trid_x = Tridiagonal(x_cord);
		Vector<Double> trid_y = Tridiagonal(y_cord);
		
		temp.addElement(data.get(0));
		for (int i=0; i<data.size()-2; i++)
			temp.addElement(new Point2D.Double(trid_x.get(i), trid_y.get(i)));
		temp.addElement(data.lastElement());
		return temp;
	}
}
