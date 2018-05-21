Enter /project and input "mvn clean install"
Then input "java -cp target/project-1.0-SNAPSHOT.jar osm.analysis.google Main" plus arguments below

1.To input a list of origin/dest:
--list-of-origin/dest   (JSON format)

2.To generate a number of random routes:
--generate-random-origins (integer number)
--radius-of-dest (unit: km)

--dist-diff-reporting-threshold-percent  (DEFAULT: ...)
--dist-diff-reporting-threshold-km 5
--dist-diff-reporting-threshold-percent  (DEFAULT: 20)
--dist-diff-reporting-threshold-km (DEFAULT: 5)
 
--time-diff-reporting-threshold-percent  (DEFAULT: 20)

Example 1

--list-of-origin/dest {routes:[{ori:{lat:-37.6941552,lon:144.5793929},dest:{lat:-34.2968327,lon:147.5588931}},{ori:{lat:-36.0905765,lon:146.9311751},dest:{lat:-35.5126728,lon:148.6093932}},{ori:{lat:-34.3001501,lon:142.1915312},dest:{lat:-35.6469135,lon:148.6763364}},{ori:{lat:-34.3607745,lon:148.025851},dest:{lat:-36.6893697,lon:142.4108182}},{ori:{lat:-34.5535703,lon:146.4113448},dest:{lat:-34.6079865,lon:147.1291069}},{ori:{lat:-37.9158962,lon:142.4296366},dest:{lat:-37.1788221,lon:144.0485408}},{ori:{lat:-36.3472808,lon:146.8028536},dest:{lat:-37.2747424,lon:149.0709654}},{ori:{lat:-37.6874412,lon:144.4336144},dest:{lat:-37.7571033,lon:144.9347327}},{ori:{lat:-36.4217525,lon:148.6168595},dest:{lat:-36.1342025,lon:146.9038279}},{ori:{lat:-35.2469941,lon:149.0977115},dest:{lat:-36.1342025,lon:141.284169}}]} --time-diff-reporting-threshold-percent 5

Example 2

--list-of-origin/dest {routes:[{ori:{lat:-37.6941552,lon:144.5793929},dest:{lat:-34.2968327,lon:147.5588931}},{ori:{lat:-36.0905765,lon:146.9311751},dest:{lat:-35.5126728,lon:148.6093932}},{ori:{lat:-34.3001501,lon:142.1915312},dest:{lat:-35.6469135,lon:148.6763364}},{ori:{lat:-34.3607745,lon:148.025851},dest:{lat:-36.6893697,lon:142.4108182}},{ori:{lat:-34.5535703,lon:146.4113448},dest:{lat:-34.6079865,lon:147.1291069}},{ori:{lat:-37.9158962,lon:142.4296366},dest:{lat:-37.1788221,lon:144.0485408}},{ori:{lat:-36.3472808,lon:146.8028536},dest:{lat:-37.2747424,lon:149.0709654}},{ori:{lat:-37.6874412,lon:144.4336144},dest:{lat:-37.7571033,lon:144.9347327}},{ori:{lat:-36.4217525,lon:148.6168595},dest:{lat:-36.1342025,lon:146.9038279}},{ori:{lat:-35.2469941,lon:149.0977115},dest:{lat:-36.1342025,lon:141.284169}}]} --time-diff-reporting-threshold-hr 0.5

Example 3

--generate-random-origins 20 --radius-of-dest 600 --dist-diff-reporting-threshold-percent 3
