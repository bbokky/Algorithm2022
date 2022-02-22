package com.algo.graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_5427_불_BFS {
	/**
	 * . : 빈공간 
	 * # : 벽 
	 * @ : 상근이 시작위치 
	 * * : 불 */
	// 매 초 불이 4방향으로 퍼진다.
	// 불이 붙으려는 칸으로 이동 불가. (불 먼저-> 그다음 상근이 이동)
	// 불이 먼저니까 상근이가 있는곳에 불이 갈 수 있음.
	// 탈출하는데 가장 빠른시간 출력 -> BFS
	static int T;
	static int N,M;
	static int sy,sx;
	static char[][] map;
	static int[][] answer;
	static boolean[][] visit;
	static Queue<int[]> sque;
	static Queue<int[]> gFires;
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		T = Integer.parseInt(br.readLine());
		
		for(int t=1; t<=T; t++) {
			st = new StringTokenizer(br.readLine());
			M = Integer.parseInt(st.nextToken()); //w (1~1,000)
			N = Integer.parseInt(st.nextToken()); //h (1~1,000)
			map = new char[N][M];
			answer = new int[N][M];
			boolean[][] fires = new boolean[N][M];
			visit = new boolean[N][M];
			sque = new LinkedList<int[]>();
			gFires = new LinkedList<int[]>();
			
			//상근이의 시작위치
			sy=0;
			sx=0;
			for(int i=0; i<N; i++) {
				String row = br.readLine();
				for(int j=0; j<M; j++) {
					map[i][j] = row.charAt(j);
					if(map[i][j]=='@') { // 상근이 
						sy = i;
						sx = j;
					}
					if(map[i][j]=='*') { // 불
						fires[i][j] = true;
						gFires.add(new int[] {i,j}); // 처음 불 시작점.
					}
				}
			}//end input
			
			sque.add(new int[] {sy,sx}); //상근이 시작위치
			answer[sy][sx]=1;
			visit[sy][sx] = true;
			go(1);
			
		}// test case 
		
	}
	
	static int[] dy = {-1,1,0,0}; // 상,하,좌,우 
	static int[] dx = {0,0,-1,1}; 
	private static void go(int time) {
		
		// 탈출 실패
//		if(time > N*M) {
//			System.out.println("IMPOSSIBLE");
//			return;
//		}
		//탈출 성공 
		if( 0==sy || sy==N-1 || 0==sx || sx==M-1) {
			System.out.println(time);
			return;
		}
		
		// 다음턴 불들... 
		Queue<int[]> nextFires = new LinkedList<>();
		
		// 1. 불 이동.
		while(!gFires.isEmpty()){
			int[] cur = gFires.poll();
			int cy = cur[0];
			int cx = cur[1];
			
			for(int d=0; d<4; d++) {
				int ny = cy+dy[d];
				int nx = cx+dx[d];
				if(!canGo(ny,nx)) continue;
				if(map[ny][nx]=='.' || map[ny][nx]=='@') {
					map[ny][nx] = '*';
					nextFires.add(new int[] {ny,nx});
				}
			}
		}// end while 
		
		// static 변수에 값 옮겨담기 
		while(!nextFires.isEmpty()) {
			gFires.add(nextFires.poll());
		}
		
		// 2.상근이 이동 
		int slen = sque.size();
		while(!sque.isEmpty()) {
			slen--;
			if(slen<0) break;
			int[] cur = sque.poll();
			int cy = cur[0];
			int cx = cur[1];
			
			if(cy==0 || cy==N-1 || cx==0 || cx==M-1) {
				System.out.println(answer[cy][cx]);
				return;
			}
			
			for(int d=0; d<4; d++) {
				int ny = cy+dy[d];
				int nx = cx+dx[d];
				if(!canGo(ny,nx)) continue;
				if(visit[ny][nx]) continue;
				if(map[ny][nx]=='.') {
					sque.add(new int[] {ny,nx});
					map[ny][nx]='@';
					visit[ny][nx]=true;
					answer[ny][nx]=answer[cy][cx]+1;
				}
			}
		}// end while
		
		if(sque.size()==0) {
			System.out.println("IMPOSSIBLE");
			return;
		}
		
		go(time+1);
						
	}
	
	private static boolean canGo(int y, int x) {
		if(0<=y && y<N && 0<= x && x<M) return true;
		return false;
	}
}
