#include <stdio.h>
#define N 500
#include <omp.h>
#include <math.h>

int main(void){
  int i,j,n,nend;
  double xleft,xright,y1,y2;
  double x[N+1],y[N+1],dx;
  double u[N+1][N+1],uu[N+1][N+1];
  double c1,c2,dt,tend;

  xleft=0.0;
  xright=100.0;
  y1=0.0;
  y2=100.0;
  dx = (xright - xleft)/(double)N;

  c1=0.5;
  c2=0.5;
  dt=0.5*dx;
  tend = 100.0;
  nend = (int)tend/dt;

  for(i=0;i<N+1;i++){
    x[i]=((double)i)*dx;
    y[i]=((double)i)*dx;
  }

  for(i=0;i<N+1;i++){
    for(j=0;j<N+1;j++){
    if(i<=N/2 && j<=N/2) 
      u[i][j]=1.0;
    else
      u[i][j]=0.0;
    }
  }


  for(n=0;n<=nend;n++){
    
#pragma omp parallel private(i,j)
{
#pragma omp for
    for(i=1;i<N-1;i++)
      for(j=1;j<N-1;j++){
	uu[i][j]=u[i][j]-c1*dt/dx*(u[i][j]-u[i-1][j])-c2*dt/dx*(u[i][j]-u[i][j-1]);
    }
#pragma omp for
    for(i=0;i<N-1;i++){
      uu[0][i]=uu[1][i];
      uu[N][i]=uu[N-1][i];
    }
#pragma omp for
    for(i=0;i<N-1;i++){
      uu[i][0]=uu[i][1];
      uu[i][N]=uu[i][N-1];
    }
#pragma omp for
    for(i=0;i<N+1;i++)
      for(j=0;j<=N+1;j++){
	u[i][j]=uu[i][j];
      }
    
 }

}
  
  for(i=0;i<N+1;i++)
    for(j=0;j<N+1;j++){
      if(i==j){
	printf("%10.5f %10.5f\n",sqrt(x[i]*x[i]+y[j]*y[j]),u[i][j]);
      }
    }
  return 0;
}
