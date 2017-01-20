#include <stdio.h>
#include <omp.h>

int main(void){
  double a[3][3],b[3][3],c[3][3];
  int i,j,k;
  for(i=0;i<3;i++)
    for(j=0;j<3;j++){
      a[i][j]=1*i-1*j;
      b[i][j]=-1*i+10*j;
    }
  
#pragma omp parallel private(j,k)
  {
  
#pragma omp for schedule(static,25)
    for(i=0;i<3;i++){
      for(j=0;j<3;j++){
	for(k=0;k<3;k++){
	  c[i][j]+=a[i][k]*b[k][j];
	}
      }
    }
  }
  printf("%lf\n",c[2][2]);
  return 0;	  
}
