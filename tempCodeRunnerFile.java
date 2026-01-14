 for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                m.setStation(i, j, new N(types[rand.nextInt(types.length)]));
            }
        }