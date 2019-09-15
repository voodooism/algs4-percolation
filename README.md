Percolation problem
===================
#### Week 1 Programming assignment of Course "Algorithms, Part 1" from Princeton University in Coursera

**What the percolation is?**  
 Given a composite systems comprised of randomly distributed insulating and metallic materials:
 what fraction of the materials need to be metallic so that the composite system is an electrical conductor?
 Given a porous landscape with water on the surface (or oil below), under what conditions will the water
 be able to drain through to the bottom (or the oil to gush through to the surface)? 
 Scientists have defined an abstract process known as percolation to model such situations.
 
The _Percolation_ class implements percolation system using an n-by-n grid of sites.  
Each site is either open or blocked.
A full site is an open site that can be connected to an open site in
the top row via a chain of neighboring (left, right, up, down) open sites.
We say the system percolates if there is a full site in the bottom row.

The _PercolationStats_ class do Monte Carlo simulation.  
It creates n-by-n grid of sites and do M trials.
Each trial is done as follows:  
* Choose a site uniformly at random among all blocked sites.
* Open the site.
* Repeat until the system percolates.

The fraction of sites that are opened when the system percolates
provides an estimate of the percolation threshold.


**Features**:  
* "Backwash" problem is solved!
* Low memory consumption (9.00 n^2 + 0.00 n + 160.00   (R^2 = 1.000))
* All test are passed including bonus memory test

It’s not necessary to use virtual sites to solve "backwash" problem.  
The main idea is to use only one "Weighted QuickUnion Union-Find" structure (N * N) and a byte[n * n] to store info about each site's state. 
I used three statuses: OPEN(0b001), CONNECTED_TOP(0b010), CONNECTED_BOTTOM(0b100).
Byte operations have helped me to combine several statuses like OPEN and CONNECTED_TOP(OPEN | CONNECTED_TOP = 0b011).
