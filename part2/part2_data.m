[Seqno, Agap] = textread('generator.data', '%d %d');
[Seqno1, Dgap] = textread('sink.data', '%d %d');

%N is number of packets in a train, L is packet size of the train
N = 100;
L = 400;

%Need to fill in this manually.
Delay = 155912405972 - 155912391561 ;

A_list = zeros(1,N);
D_list = zeros(1,N);
A_list(1) = 0;
D_list(1) = 0 + Delay;
for i = 2:N
    A_list(i) = A_list(i-1) + Agap(i);
    D_list(i) = D_list(i-1) + Dgap(i);
end

A_list = transpose(A_list);
D_list = transpose(D_list);
% LSR for departure function
dept = polyfit(D_list/1000,Seqno1*L*8,1);

%plotting packet train
figure(1);
plot(A_list/1000,Seqno*8*L,D_list/1000,Seqno1*8*L);
title('Arrival and Departure functions');
ylabel('Accumulated Bits');
xlabel('Time(ms)');

% find the max backlog
lines = findobj(gcf, 'type', 'line');
CurveHandle1 = lines(1);
CurveHandle2 = lines(2);
x1 = get(CurveHandle1, 'XData');
y1 = get(CurveHandle1, 'YData');
x2 = get(CurveHandle2, 'XData');
y2 = get(CurveHandle2, 'YData');
projectedy2 = interp1(x2,y2,x1);
[maxdist,maxidx] = max(abs(projectedy2-y2));
[mindist,minidx] = min(abs(projectedy2-y2));

fprintf(1,'Maximum backlog is %g bits at t = %g ms\n', maxdist, x1(maxidx));
fprintf(1,'Departure rate is %g kbps\n',dept(1));
Delay = Delay / 1000;
fprintf(1,'Delay is %g ms\n', Delay);
legend('Arrivals','Departures');
