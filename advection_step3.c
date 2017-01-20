#include <stdio.h>
#include <math.h>
#define N 100

int main(void){
  int i,n,nend;
  double xleft,xright;
  double x[N+1],dx;
  double u[N+1],uu[N+1];
  double c,dt,tend;
  double rad;

  xleft=0.0;
  xright=100.0;
  dx = (xright - xleft)/(double)N;

  c=0.5;
  dt=0.5*dx;
  tend=50.0*dt;
  nend=(int)tend/dt;
  rad=M_PI/((double)N+1);
  for(i=0;i<N+1;i++){
    x[i]=((double)i)*dx;
  }


  for(i=0;i<N+1;i++){
    u[i]=sin(rad*i);
   
  }

  for(n=0;n<=nend;n++){
    uu[0]=u[1];
    uu[N]=u[N-1];
    
    for(i=1;i<N;i++){
      uu[i]=u[i]-c*dt/dx*(u[i]-u[i-1]);
    }
    for(i=0;i<N+1;i++){
      u[i]=uu[i];
    }
  }
  for(i=0;i<N+1;i++){
    printf("%10.5f %10.5f\n",x[i],u[i]);
  }
  return 0;
}
